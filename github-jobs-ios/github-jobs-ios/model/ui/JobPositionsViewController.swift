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
    
    var jobPositions = [JobPositionDTO]()

    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        refreshControl?.beginRefreshing()
        find()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override var refreshControl: UIRefreshControl? {
        get {
            let rc = UIRefreshControl()
        
            return rc
        }
        
        set {
            
        }
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
        cell.jobTypeLabel.text = dto.title
        cell.createdTimeLabel.text = ""
        cell.companyNameLabel.text = dto.company
        cell.jobTypeLabel.text = dto.type

        return cell
    }
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    private func find() {
        GithubJobApi.findJobPositions(description: nil, location: nil) { [weak self] resp in
            switch resp {
            case .success(let data):
                self?.jobPositions = data
                self?.tableView.reloadData()
                self?.refreshControl?.endRefreshing()
            case .error(let error):
                print(error)
            }
        }
    }
}

extension UIImageView {
    
    func load(url: String) {
        
        Alamofire.request(url).responseImage { [weak self] resp in
            if let image = resp.result.value {
                self?.image = image
            } else {
                
            }
        }
    }
    
}
