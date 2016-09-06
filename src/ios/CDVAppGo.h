//
//  CDVAppGo.h
//  ionic-quickstarter-with-gulp
//
//  Created by tangyijie on 16/9/5.
//
//

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

@interface CDVAppGo : CDVPlugin

@property (nonatomic, strong) NSDictionary *appInfo;

- (void)appGo:(CDVInvokedUrlCommand*)command;
- (void)checkAppInstalled:(CDVInvokedUrlCommand*)command;

@end
