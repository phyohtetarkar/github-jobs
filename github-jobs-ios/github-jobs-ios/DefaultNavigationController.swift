//
//  DefaultNavigationController.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 5/21/20.
//  Copyright © 2020 Phyo Htet Arkar. All rights reserved.
//

import UIKit

class DefaultNavigationController: UINavigationController {

    override func viewDidLoad() {
        super.viewDidLoad()

        if #available(iOS 13.0, *) {
            let style = UINavigationBarAppearance()
            style.backgroundColor = #colorLiteral(red: 0.1294117647, green: 0.5843137255, blue: 0.9529411765, alpha: 1)
            style.titleTextAttributes = [
                NSAttributedString.Key.foregroundColor: UIColor.white,
                NSAttributedString.Key.font: UIFont.systemFont(ofSize: 17, weight: .heavy)
            ]
            style.shadowImage = nil
            style.shadowColor = nil
            self.navigationBar.standardAppearance = style
        } else {
            self.navigationBar.setBackgroundImage(UIImage(), for: UIBarMetrics.default)
            self.navigationBar.shadowImage = UIImage()
        }
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }

}
