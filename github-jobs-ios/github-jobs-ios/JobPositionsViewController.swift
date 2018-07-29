//
//  ViewController.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 7/29/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import UIKit

class JobPositionsViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
//        GithubJobApi.findJobPositions(description: "java", location: nil, success: { result in
//            result.forEach { print($0.createdAt ?? "null", separator: "", terminator: "\n") }
//        }, failure: { error in
//            print(error)
//        })
        
//        GithubJobApi.getJobPosition(id: "48b90746-91a3-11e8-8f4a-256256407806", success: { result in
//            print(result)
//        }, failure: { error in
//            print(error)
//        })
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}

