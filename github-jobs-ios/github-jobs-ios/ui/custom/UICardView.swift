//
//  UICardView.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 7/31/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import UIKit

@IBDesignable
class UICardView: UIView {
    
    @IBInspectable var cornerRadius: CGFloat = 2
    @IBInspectable var shadowOffsetWidth: Int = 0
    @IBInspectable var shadowOffsetHeight: Int = 1
    @IBInspectable var shadowColor: UIColor? = UIColor.black
    @IBInspectable var fillColor: UIColor? = UIColor.white
    @IBInspectable var shadowOpacity: Float = 0.2
    
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
//    override func draw(_ rect: CGRect) {
//
//    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        let shadowLayer = CAShapeLayer()
            
        shadowLayer.path = UIBezierPath(roundedRect: bounds, cornerRadius: cornerRadius).cgPath
        shadowLayer.fillColor = fillColor?.cgColor
            
        shadowLayer.shadowColor = shadowColor?.cgColor
        shadowLayer.shadowPath = shadowLayer.path
        shadowLayer.shadowOffset = CGSize(width: shadowOffsetWidth, height: shadowOffsetHeight)
        shadowLayer.shadowOpacity = shadowOpacity
        shadowLayer.shadowRadius = 1
            
        layer.insertSublayer(shadowLayer, at: 0)
    }
 
}
