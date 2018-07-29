//
//  GithubJobApi.swift
//  github-jobs-ios
//
//  Created by Phyo Htet Arkar on 7/29/18.
//  Copyright © 2018 Phyo Htet Arkar. All rights reserved.
//

import Foundation
import Alamofire

class GithubJobApi {
    
    static let BASE_URL = "https://jobs.github.com/"
    
    static func findJobPositions(description: String?, location: String?, fullTime: Bool = false, page: Int = 0, success: @escaping ([JobPositionDTO]) -> Void, failure: @escaping (String) -> Void) {
        
        var params: [String: Any] = ["description": description ?? "", "location": location ?? ""]
        
        if fullTime {
            params["full_time"] = fullTime
        }
        
        params["page"] = page
        
        Alamofire.request(BASE_URL + "positions.json", parameters: params).responseString { resp in
            
            switch resp.result {
            case .success(let value):
                do {
                    let json = value as String
                    let result = try JSONDecoder().decode([JobPositionDTO].self, from: json.data(using: .utf8)!)
                    success(result)
                } catch let decodeError {
                    print("Error decoding job positions \(decodeError)")
                    failure("Error loading positions")
                }
            case .failure(let error):
                failure(error.localizedDescription)
            }
            
        }
    }
    
    static func getJobPosition(id: String, success: @escaping (JobPositionDTO) -> Void, failure: @escaping (String) -> Void) {
        
        Alamofire.request(BASE_URL + "positions" + "/" + id + ".json").responseString { resp in
            
            switch resp.result {
            case .success(let value):
                do {
                    let json = value as String
                    let result = try JSONDecoder().decode(JobPositionDTO.self, from: json.data(using: .utf8)!)
                    success(result)
                } catch let decodeError {
                    print("Error decoding job positions \(decodeError)")
                    failure("Error loading position")
                }
            case .failure(let error):
                failure(error.localizedDescription)
            }
            
        }
        
    }
    
}
