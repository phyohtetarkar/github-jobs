//
//  JobPositionsViewController.swift
//  github-jobs-ios
//
//  Created by OP-Macmini3 on 7/30/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class JobPositionsViewController: UITableViewController {
    
    private var jobPositions = [JobPositionDTO]()
    private var loading = false
    private var page = 0

    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        self.refreshControl?.beginRefreshing()
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

   
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
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
        
    }
    
    override func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        let offset = jobPositions.count - 5
        
        if !loading && indexPath.row == offset {
            loadMore()
        }
        
    }
    
    private func find() {
        self.page = 0
        GithubJobApi.findJobPositions(description: nil, location: nil) { [weak self] resp in
            switch resp {
            case .success(let data):
                self?.jobPositions = data
                self?.tableView.reloadData()
            case .error(let error):
                print(error)
            }
            
            self?.refreshControl?.endRefreshing()
        }
    }
    
    private func loadMore() {
        self.loading = true
        self.page += 1
        GithubJobApi.findJobPositions(description: nil, location: nil, page: page) { [weak self] resp in
            switch resp {
            case .success(let data):
                self?.jobPositions.append(contentsOf: data)
                self?.tableView.reloadData()
            case .error(let error):
                print(error)
                self?.page -= 1
            }
            
            self?.loading = false
        }
    }
}

extension UIImageView {
    
    func load(imageUrl: String?) {
        
        if let url = imageUrl {
            self.image = UIImage(named: "loading")
            Alamofire.request(url).responseImage { [weak self] resp in
                if let image = resp.result.value {
                    self?.image = image
                } else {
                    self?.image = UIImage(named: "placeholder")
                }
            }
        } else {
            self.image = UIImage(named: "placeholder")
        }
        
    }
    
}
