//
//  JobPositionDetailViewController.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 7/31/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import UIKit

class JobPositionDetailViewController: UIViewController {
    
    var jobPosition: JobPositionDTO? = nil

    @IBOutlet weak var companyLogoImageView: UIImageView!
    @IBOutlet weak var companyTitleLabel: UILabel!
    @IBOutlet weak var companyWebSiteLabel: UILabel!
    @IBOutlet weak var howToApplyTextView: UITextView!
    @IBOutlet weak var descriptionTextView: UITextView!
    @IBOutlet weak var jobTitleLabel: UILabel!
    @IBOutlet weak var jobTypeLocationLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        howToApplyTextView.textContainer.lineFragmentPadding = .zero
        descriptionTextView.textContainer.lineFragmentPadding = .zero
        
        if let dto = jobPosition {
            companyLogoImageView.load(imageUrl: dto.companyLogo)
            companyTitleLabel.text = dto.company
            companyWebSiteLabel.text = dto.companyUrl
            jobTitleLabel.text = dto.title
            jobTypeLocationLabel.text = "\(dto.type) / \(dto.location)"
            
            do {
                howToApplyTextView.attributedText = try NSAttributedString(data: dto.howToApply.data(using: .utf8)!, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding:String.Encoding.utf8.rawValue], documentAttributes: nil)
            } catch let error {
                print(error)
            }
            
            do {
                descriptionTextView.attributedText = try NSAttributedString(data: dto.description.data(using: .utf8)!, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding:String.Encoding.utf8.rawValue], documentAttributes: nil)
            } catch let error {
                print(error)
            }
            
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onCompanyLinkTab(_ sender: UITapGestureRecognizer) {
        if let url = jobPosition?.companyUrl {
            do {
                try UIApplication.shared.open(url.asURL(), options: [:])
            } catch let error {
                print(error)
            }
        }
        
    }
}
