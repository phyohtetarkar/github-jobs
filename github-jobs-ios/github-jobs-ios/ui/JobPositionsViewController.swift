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

class JobPositionsViewController: UITableViewController {
    
    private var jobPositions = [JobPositionDTO]()
    private var loading = false
    private var page = 0
    
    private var desc: String? = nil
    private var location: String? = nil
    private var fulltime = false

    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        self.refreshControl?.addTarget(self, action: #selector(find), for: UIControlEvents.valueChanged)
        
        find()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return jobPositions.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "CellJobPosition", for: indexPath) as? JobPositionViewCell else {
            fatalError("The dequeued cell is not an instance of JobPositionViewCell.")
        }
        
        let dto = jobPositions[indexPath.row]
        cell.jobTitleLabel.text = dto.title
        cell.createdTimeLabel.text = dto.createdAt?.timeAgoDisplay()
        cell.companyNameLabel.text = dto.company
        cell.jobTypeLabel.text = dto.type
        cell.companyImageView.load(imageUrl: dto.companyLogo)

        return cell
    }
    
    override func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
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
            
            let dto = jobPositions[indexPath.row]
            jobPositionDetailViewController.jobPosition = dto
        case "filter":
            guard let navigationViewController = segue.destination as? UINavigationController, let jobPositionFilterViewController = navigationViewController.topViewController as? JobPositionsFilterViewController else {
                return
            }
            print("desc: \(desc ?? ""), location: \(location ?? ""), fulltime: \(fulltime)")
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
        
        find()
    }
    
    @objc private func find() {
        self.refreshControl?.beginRefreshing()
        self.page = 0
        GithubJobApi.cancelAllRequests()
        GithubJobApi.findJobPositions(description: desc, location: location, fullTime: fulltime) { [weak self] resp in
            switch resp {
            case .success(let data):
                self?.jobPositions = data
                self?.tableView.reloadData()
            case .error(let error):
                self?.showAlert(msg: error)
            }
            
            self?.refreshControl?.endRefreshing()
        }
    }
    
    private func loadMore() {
        self.loading = true
        self.page += 1
        GithubJobApi.findJobPositions(description: desc, location: location, fullTime: fulltime, page: page) { [weak self] resp in
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
        }
    }
    
    private func showAlert(msg: String) {
        let alert = UIAlertController(title: "Error", message: msg, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        
        present(alert, animated: true, completion: nil)
    }
}

extension UIImageView {
    
    func load(imageUrl: String?) {
        
        if let url = imageUrl {
            self.image = UIImage(named: "loading")
            Alamofire.request(url).responseImage { [weak self] resp in
                switch resp.result {
                case .success(let value):
                    self?.image = value
                case .failure(let error):
                    print(error.localizedDescription)
                    self?.image = UIImage(named: "placeholder")
                }
            }
        } else {
            self.image = UIImage(named: "placeholder")
        }
        
    }
    
}
