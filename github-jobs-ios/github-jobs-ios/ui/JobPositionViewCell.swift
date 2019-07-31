//
//  JobPositionViewCell.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 7/30/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

let imageCache = AutoPurgingImageCache(
    memoryCapacity: 100_000_000,
    preferredMemoryUsageAfterPurge: 60_000_000
)

class JobPositionViewCell: UITableViewCell {

    @IBOutlet weak var companyImageView: UIImageView!
    @IBOutlet weak var jobTitleLabel: UILabel!
    @IBOutlet weak var createdTimeLabel: UILabel!
    @IBOutlet weak var companyNameLabel: UILabel!
    @IBOutlet weak var jobTypeLabel: UILabel!
    
    var dataRequest: DataRequest?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func bind(_ dto: JobPositionDTO) {
        dataRequest?.cancel()
        
        self.jobTitleLabel.text = dto.title
        self.createdTimeLabel.text = dto.createdAt?.timeAgoDisplay()
        self.companyNameLabel.text = dto.company
        self.jobTypeLabel.text = dto.type
        dataRequest = self.companyImageView.load(imageUrl: dto.companyLogo)
    }
}

extension UIImageView {
    
    @discardableResult
    func load(imageUrl: String?) -> DataRequest? {
        if let urlStr = imageUrl, let url = URL(string: urlStr) {
            let urlRequest = URLRequest(url: url)
            self.image = UIImage(named: "loading")
            
            if let cachedImage = imageCache.image(for: urlRequest) {
                self.image = cachedImage
            } else {
                return Alamofire.request(urlRequest).responseImage { [weak self] resp in
                    switch resp.result {
                    case .success(let value):
                        self?.image = value
                        imageCache.add(value, for: urlRequest)
                    case .failure(let error):
                        print(error.localizedDescription)
                        self?.image = UIImage(named: "placeholder")
                    }
                }
            }
        } else {
            self.image = UIImage(named: "placeholder")
        }
        
        return nil
    }
    
}
