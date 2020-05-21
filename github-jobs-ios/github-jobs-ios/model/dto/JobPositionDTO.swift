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
        dateFormatter.dateFormat = "E MMM dd HH:mm:ss zzz yyyy"
        let created = try container.decode(String.self, forKey: .createdAt)
        createdAt = dateFormatter.date(from: created)
        
        title = try container.decode(String.self, forKey: .title)
        location = try container.decode(String.self, forKey: .location)
        type = try container.decode(String.self, forKey: .type)
        description = try container.decode(String.self, forKey: .description)
        howToApply = try container.decode(String.self, forKey: .howToApply)
        company = try container.decode(String.self, forKey: .company)
        companyUrl = try container.decodeIfPresent(String.self, forKey: .companyUrl)
        companyLogo = try container.decodeIfPresent(String.self, forKey: .companyLogo)
        url = try container.decode(String.self, forKey: .url)
    }
    
}

extension Date {
    func timeAgoDisplay() -> String {
        
        let calendar = Calendar.current
        let minuteAgo = calendar.date(byAdding: .minute, value: -1, to: Date())!
        let hourAgo = calendar.date(byAdding: .hour, value: -1, to: Date())!
        let dayAgo = calendar.date(byAdding: .day, value: -1, to: Date())!
        let weekAgo = calendar.date(byAdding: .day, value: -7, to: Date())!
        
        if minuteAgo < self {
            let diff = Calendar.current.dateComponents([.second], from: self, to: Date()).second ?? 0
            return "\(diff) sec ago"
        } else if hourAgo < self {
            let diff = Calendar.current.dateComponents([.minute], from: self, to: Date()).minute ?? 0
            return "\(diff) min ago"
        } else if dayAgo < self {
            let diff = Calendar.current.dateComponents([.hour], from: self, to: Date()).hour ?? 0
            return "\(diff) hrs ago"
        } else if weekAgo < self {
            let diff = Calendar.current.dateComponents([.day], from: self, to: Date()).day ?? 0
            return "\(diff) days ago"
        }
        let diff = Calendar.current.dateComponents([.weekOfYear], from: self, to: Date()).weekOfYear ?? 0
        return "\(diff) weeks ago"
    }
    
    func getElapsedInterval() -> String {
        
        let interval = Calendar.current.dateComponents([.year, .month, .day, .hour, .minute, .second], from: self, to: Date())
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd MMM yyyy"
        
        if let year = interval.year, year > 0 {
            return dateFormatter.string(from: self)
        } else if let month = interval.month, month > 0 {
            return dateFormatter.string(from: self)
        } else if let day = interval.day, day > 0 {
            return day == 1 ? "Yesterday" :
                "\(day) days ago"
        } else if let hour = interval.hour, hour > 0 {
            return hour == 1 ? "\(hour) hour ago" :
                "\(hour) hours ago"
        } else if let minute = interval.minute, minute > 0 {
            return minute == 1 ? "\(minute) minute ago" :
                "\(minute) minutes ago"
        } else if let second = interval.second, second > 0 {
            return second == 1 ? "\(second) second ago" :
                "\(second) seconds ago"
        } else {
            return "a moment ago"
        }
        
    }
}
