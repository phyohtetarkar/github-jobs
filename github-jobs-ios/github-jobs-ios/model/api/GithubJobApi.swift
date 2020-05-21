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
    
    private static let BASE_URL = "https://jobs.github.com"
    
    enum ApiResponse<T> {
        case success(T)
        case error(String)
    }
    
    static func findJobPositions(description: String?, location: String?, fullTime: Bool = false, page: Int = 1, completion: @escaping (ApiResponse<[JobPositionDTO]>) -> Void) -> DataRequest {
        
        let url = "\(BASE_URL)/positions.json"
        
        var params: [String: Any] = ["description": description ?? "", "location": location ?? ""]
        
        if fullTime {
            params["full_time"] = fullTime
        }
        
        params["page"] = page
        
        AF.request(url, parameters: params).responseDecodable(of: [JobPositionDTO].self) { resp in
            switch resp.result {
            case .success(let value):
                completion(ApiResponse.success(value))
            case .failure(let error):
                completion(ApiResponse.error(error.localizedDescription))
            }
        }
        
        return AF.request(url, parameters: params).responseString { resp in
            
            switch resp.result {
            case .success(let value):
                do {
                    let json = value as String
                    let result = try JSONDecoder().decode([JobPositionDTO].self, from: json.data(using: .utf8)!)
                    completion(ApiResponse.success(result))
                } catch {
                    //print("Error decoding job positions \(decodeError)")
                    completion(ApiResponse.error("Error loading positions"))
                }
            case .failure(let error):
                completion(ApiResponse.error(error.localizedDescription))
            }
            
        }
    }
    
    static func getJobPosition(id: String, completion: @escaping (ApiResponse<JobPositionDTO>) -> Void) -> DataRequest {
        
        let url = "\(BASE_URL)/positions/\(id).json"
        
        return AF.request(url).responseDecodable(of: JobPositionDTO.self) { resp in
            switch resp.result {
            case .success(let value):
                completion(ApiResponse.success(value))
            case .failure(let error):
                completion(ApiResponse.error(error.localizedDescription))
            }
            
        }
        
    }
    
    static func cancelAllRequests() {
        AF.session.getTasksWithCompletionHandler {(sessionData, uploadData, downloadData) in
            sessionData.forEach { $0.cancel() }
            uploadData.forEach { $0.cancel() }
            downloadData.forEach { $0.cancel() }
        }
    }
    
}
