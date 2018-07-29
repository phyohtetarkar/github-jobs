//
//  JobPositionDTO.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 7/29/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import Foundation

struct JobPositionDTO: Decodable {
    let id: String
    let createdAt: Date?
    let title: String
    let location: String
    let type: String
    let description: String
    let howToApply: String
    let company: String
    let companyUrl: String?
    let companyLogo: String?
    let url: String
    
    enum CodingKeys: String, CodingKey {
        case id
        case createdAt = "created_at"
        case title
        case location
        case type
        case description
        case howToApply = "how_to_apply"
        case company
        case companyUrl = "company_url"
        case companyLogo = "company_logo"
        case url
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(String.self, forKey: .id)
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "E MMM dd hh:mm:ss Z yyyy"
        let created = try container.decode(String.self, forKey: .createdAt)
        createdAt = dateFormatter.date(from: created)
        
        title = try container.decode(String.self, forKey: .title)
        location = try container.decode(String.self, forKey: .location)
        type = try container.decode(String.self, forKey: .type)
        description = try container.decode(String.self, forKey: .description)
        howToApply = try container.decode(String.self, forKey: .howToApply)
        company = try container.decode(String.self, forKey: .company)
        companyUrl = try container.decode(String.self, forKey: .companyUrl)
        companyLogo = try container.decode(String.self, forKey: .companyLogo)
        url = try container.decode(String.self, forKey: .url)
    }
    
}
