//
//  ActivityIndicatorView.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 8/1/19.
//  Copyright © 2019 Phyo Htet Arkar. All rights reserved.
//

import UIKit

@IBDesignable
class RoundedView: UIView {
    

    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        self.layer.cornerRadius = 2
    }

}
