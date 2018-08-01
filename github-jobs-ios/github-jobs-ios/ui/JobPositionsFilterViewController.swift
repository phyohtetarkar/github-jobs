//
//  JobPositionsFilterViewController.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 8/1/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import UIKit

class JobPositionsFilterViewController: UIViewController {

    @IBOutlet weak var descriptionTextField: UITextField!
    @IBOutlet weak var locationTextField: UITextField!
    @IBOutlet weak var fullTimeSwitch: UISwitch!
    
    var desc: String? = nil
    var location: String? = nil
    var fulltime = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        descriptionTextField.text = desc
        locationTextField.text = location
        fullTimeSwitch.isOn = fulltime
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func onCancel(_ sender: UIBarButtonItem) {
        dismiss(animated: true, completion: nil)
    }
}
