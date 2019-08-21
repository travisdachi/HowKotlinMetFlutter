//
//  ViewController.swift
//  MyiOS
//
//  Created by Travis P on 21/8/2562 BE.
//  Copyright © 2562 Travis P. All rights reserved.
//

import UIKit
import Flutter

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func onGoFlutterClick(_ sender: Any) {
        let flutterVC = FlutterViewController(nibName: nil, bundle: nil)
        present(flutterVC, animated: true, completion: nil)
    }
    
}

