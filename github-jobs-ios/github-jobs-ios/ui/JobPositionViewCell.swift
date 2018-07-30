//
//  JobPositionViewCell.swift
//  github-jobs-ios
//
//  Created by OP-Macmini3 on 7/30/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import UIKit

class JobPositionViewCell: UITableViewCell {

    @IBOutlet weak var companyImageView: UIImageView!
    @IBOutlet weak var jobTitleLabel: UILabel!
    @IBOutlet weak var createdTimeLabel: UILabel!
    @IBOutlet weak var companyNameLabel: UILabel!
    @IBOutlet weak var jobTypeLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
