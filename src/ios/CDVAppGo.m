//
//  CDVAppGo.m
//  ionic-quickstarter-with-gulp
//
//  Created by tangyijie on 16/9/5.
//
//

#import "CDVAppGo.h"
#import <Cordova/CDV.h>

@implementation CDVAppGo

- (void)checkAppInstalled:(CDVInvokedUrlCommand*)command{
    NSDictionary * argums = command.arguments[0];
    CDVPluginResult* pluginResult = nil;
    NSString *urlSchema = [argums objectForKey:@"urlSchema"];
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@://",urlSchema]];
    if ([[UIApplication sharedApplication]canOpenURL:url]){
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:true];
        
    }else{
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:false];
        
    }
    [self.commandDelegate sendPluginResult: pluginResult callbackId:command.callbackId];
}



- (void)appGo:(CDVInvokedUrlCommand*)command{
    NSDictionary * argums = command.arguments[0];
    NSLog(@"====>>>>>>>>>>>>%@",argums);
    NSString *urlSchema = [argums objectForKey:@"urlSchema"];
    NSString *suffixText = [argums objectForKey:@"suffixText"];
//    NSString *appType = [argums objectForKey:@"appType"];//"appType": "appstore/inhouse/",
//    NSString *downUrl = [argums objectForKey:@"downloadUrl"];
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@://%@",urlSchema,suffixText]];
    
    if ([[UIApplication sharedApplication]canOpenURL:url]){
        
        [[UIApplication sharedApplication] openURL:url];
        
    }else{
        _appInfo = [NSDictionary new];
        _appInfo = argums;

        NSLog(@"=========app not installed>>>>>>>>>>>>");
        UIAlertView * locationAlert =
        [[UIAlertView alloc] initWithTitle:@"还没有安装此APP,确定是否安装！"
                                   message:nil
                                  delegate:self
                         cancelButtonTitle:@"取消"
                         otherButtonTitles:@"确定", nil];
        
        [locationAlert show];
    
        
    }

}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSString* msg = [[NSString alloc] initWithFormat:@"您按下的第%ld个按钮！",(long)buttonIndex];
    
    if(buttonIndex == 1){
        NSString *downloadUrlString = [_appInfo objectForKey:@"downloadUrl"];
        NSString *appType = [_appInfo objectForKey:@"appType"];
        NSURL *downloadUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@",downloadUrlString]];
        //        应用未安装情况
        if ([appType isEqualToString:@"inhouse"]) {
            //            通过跳转url下载app   downloadUrl
            [[UIApplication sharedApplication] openURL:downloadUrl];
            
        }
        if ([appType isEqualToString:@"appstore"]) {
            //                        通过跳转url下载app
            //            比如：http://itunes.apple.com/gb/app/yi-dong-cai-bian/id391945719?mt=8
            //            然后将 http:// 替换为 itms:// 或者 itms-apps://：
            
            [[UIApplication sharedApplication] openURL:downloadUrl];
        }
        
    }
    if(buttonIndex == 0){
        NSLog(@"=msg:====cancel=====");
    }

    
}

@end
