//
//  JobPositionsViewController.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 7/30/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class JobPositionsViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var loadMoreIndicator: UIActivityIndicatorView!
    @IBOutlet weak var loadMoreContainerView: UIView!
    @IBOutlet weak var loadMoreContainerViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var filterSearchIndicatorView: RoundedView!
    @IBOutlet weak var filterSearchIndicator: UIActivityIndicatorView!
    
    private var jobPositions = [JobPositionDTO]()
    private var loading = false
    private var page = 0
    
    private var desc: String?
    private var location: String?
    private var fulltime = false
    
    private var dataRequest: DataRequest?

    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        // toggleActivityIndicatorVisibility(hidden: true)
        
        self.tableView.dataSource = self
        self.tableView.delegate = self
        
        self.tableView.refreshControl = UIRefreshControl()
        self.tableView.refreshControl?.addTarget(self, action: #selector(find), for: UIControl.Event.valueChanged)
        
        self.tableView.refreshControl?.beginRefreshing()
        find()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        dataRequest?.cancel()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return jobPositions.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "CellJobPosition", for: indexPath) as? JobPositionViewCell else {
            fatalError("The dequeued cell is not an instance of JobPositionViewCell.")
        }
        
        let dto = jobPositions[indexPath.row]
        cell.bind(dto)

        return cell
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        let offset = jobPositions.count - 1
        
        if !loading && indexPath.row == offset {
            loadMore()
        }
        
    }
   
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        
        switch segue.identifier {
        case "detail":
            guard let jobPositionDetailViewController = segue.destination as? JobPositionDetailViewController else {
                return
            }
            
            guard let jobPositionViewCell = sender as? JobPositionViewCell else {
                return
            }
            
            guard let indexPath = tableView.indexPath(for: jobPositionViewCell) else {
                return
            }
            
            self.tableView.deselectRow(at: indexPath, animated: true)
            let dto = jobPositions[indexPath.row]
            jobPositionDetailViewController.jobPosition = dto
        case "filter":
            guard let navigationViewController = segue.destination as? UINavigationController, let jobPositionFilterViewController = navigationViewController.topViewController as? JobPositionsFilterViewController else {
                return
            }
            jobPositionFilterViewController.desc = desc
            jobPositionFilterViewController.location = location
            jobPositionFilterViewController.fulltime = fulltime
        default:
            return
        }
        
    }
    
    @IBAction func unwindFormFilter(_ sender: UIStoryboardSegue) {
        guard let vc = sender.source as? JobPositionsFilterViewController else {
            return
        }
        
        desc = vc.descriptionTextField.text
        location = vc.locationTextField.text
        fulltime = vc.fullTimeSwitch.isOn
        
        
        filterSearchIndicatorView.isHidden = false
        filterSearchIndicator.startAnimating()
        
        find()
    }
    
    @objc private func find() {
        self.page = 0
        dataRequest?.cancel()
        dataRequest = GithubJobApi.findJobPositions(description: desc, location: location, fullTime: fulltime) { [weak self] resp in
            switch resp {
            case .success(let data):
                self?.jobPositions = data
                self?.tableView.reloadData()
                self?.page += 1
            case .error(let error):
                self?.showAlert(msg: error)
            }
            
            if self?.tableView.refreshControl?.isRefreshing ?? false {
                self?.tableView.refreshControl?.endRefreshing()
            }
            
            if let v = self?.filterSearchIndicatorView, !v.isHidden {
                self?.filterSearchIndicator?.stopAnimating()
                self?.filterSearchIndicatorView?.isHidden = true
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + .microseconds(300)) {
                
                if self?.tableView.numberOfRows(inSection: 0) != 0 {
                    self?.tableView.scrollToRow(at: IndexPath(row: 0, section: 0), at: .top, animated: true)
                }
            }
            
        }
    }
    
    private func loadMore() {
        self.loading = true
        self.page += 1
        toggleActivityIndicatorVisibility(hidden: false)
        dataRequest = GithubJobApi.findJobPositions(description: desc, location: location, fullTime: fulltime, page: page) { [weak self] resp in
            switch resp {
            case .success(let data):
                if data.count > 0, let offset = self?.jobPositions.count {
                    let indexPaths = (0..<data.count).map { IndexPath(row: $0 + offset, section: 0) }
                    self?.jobPositions.append(contentsOf: data)
                    self?.tableView.beginUpdates()
                    self?.tableView.insertRows(at: indexPaths, with: .bottom)
                    self?.tableView.endUpdates()
                }
            case .error(let error):
                self?.page -= 1
                self?.showAlert(msg: error)
            }
            
            self?.loading = false
            self?.toggleActivityIndicatorVisibility(hidden: true)
        }
    }
    
    private func showAlert(msg: String) {
        let alert = UIAlertController(title: "Error", message: msg, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        
        present(alert, animated: true, completion: nil)
    }
    
    private func toggleActivityIndicatorVisibility(hidden: Bool) {
        if hidden {
            self.loadMoreIndicator.stopAnimating()
            self.loadMoreContainerViewHeightConstraint.constant = 0
        } else {
            self.loadMoreIndicator.startAnimating()
            self.loadMoreContainerViewHeightConstraint.constant = 60
        }
        self.loadMoreContainerView.isHidden = hidden
       
        self.loadMoreContainerView.layoutIfNeeded()
    }
}
