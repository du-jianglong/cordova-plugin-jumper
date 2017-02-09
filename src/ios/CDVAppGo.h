//
//  CDVAppGo.h
//  ionic-quickstarter-with-gulp
//
//  Created by tangyijie on 16/9/5.
//
//

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVScreenOrientationDelegate.h>

#ifdef __CORDOVA_4_0_0
#import <Cordova/CDVUIWebViewDelegate.h>
#else
#import <Cordova/CDVWebViewDelegate.h>
#endif

@interface CDVAppGo : CDVPlugin<UIWebViewDelegate>

@property (nonatomic, strong) NSDictionary *appInfo;
@property (nonatomic, strong) UIWebView *appWeb;
@property (nonatomic, strong) UIViewController *webVC;
@property (nonatomic, strong) NSString *finshWebUrl;

- (void)appGo:(CDVInvokedUrlCommand*)command;
- (void)checkAppInstalled:(CDVInvokedUrlCommand*)command;
- (void)appLiteGo:(CDVInvokedUrlCommand*)command;
@end


