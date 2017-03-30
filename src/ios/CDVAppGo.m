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
    
    
    
    if ([[UIApplication sharedApplication] openURL:url]){
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:true];
        
    }else{
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:false];
        
    }
    [self.commandDelegate sendPluginResult: pluginResult callbackId:command.callbackId];
}




- (void)creactWebViewWithUrl:(NSString *)url
{
    
    NSLog(@"====%@",url);
    UIViewController *webVC= [UIViewController new];
    self.webVC = webVC;
    
    UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:webVC];
   
    
    
    //设置push跳转动画
    CATransition *animation = [CATransition animation];
    animation.duration = 0.5;
    [animation setType:kCATransitionPush];
    [animation setSubtype:kCATransitionFromRight];
    [animation setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
     [[[[UIApplication sharedApplication]keyWindow]layer]addAnimation:animation forKey:nil];

    
    
    [self.viewController presentViewController:nav animated:NO completion:nil];
    webVC.view.backgroundColor = [UIColor blueColor];
    UIBarButtonItem * back =  [[UIBarButtonItem alloc] initWithTitle:@"<返回" style:UIBarButtonItemStylePlain target:self action:@selector(goback1)];
    UIBarButtonItem * cancel =  [[UIBarButtonItem alloc] initWithTitle:@"" style:UIBarButtonItemStylePlain target:self action:@selector(goback)];

    webVC.navigationItem.leftBarButtonItems = @[back,cancel];
   
    
    
  
    _appWeb = [[UIWebView alloc]initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen ].applicationFrame.size.width, [UIScreen mainScreen ].applicationFrame.size.height+20)] ;
    NSURLRequest *request =[NSURLRequest requestWithURL:[NSURL URLWithString:url]];
    
    [webVC.view addSubview:_appWeb];
    
    [_appWeb loadRequest:request];
    _appWeb.delegate = self;
    
    
}
-(void)goback
{
     [self popWebView];
}
-(void)goback1
{
    [_appWeb goBack];
    if ([self.webVC.navigationItem.leftBarButtonItems[1].title isEqualToString:@""]) {
        [self popWebView];
    }
    
}
//pop动画跳转
-(void)popWebView{
    CATransition *animation = [CATransition animation];
    animation.duration = 0.5;
    [animation setType:kCATransitionPush];
    [animation setSubtype:kCATransitionFromLeft];
    [animation setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut]];
    [[[[UIApplication sharedApplication]keyWindow]layer]addAnimation:animation forKey:nil];
    [self.webVC dismissViewControllerAnimated:NO completion:nil];
}
-(void)webViewDidFinishLoad:(UIWebView *)webView
{
    if (self.finshWebUrl.length == 0) {
        self.finshWebUrl =  [webView.request.URL absoluteString];
        NSLog(@" finshurl-----%@",self.finshWebUrl);
    }else{
        if([webView.request.URL isEqual:[NSURL URLWithString: self.finshWebUrl]]){
            NSLog(@"开始访问");
            UIBarButtonItem *item =self.webVC.navigationItem.leftBarButtonItems[1];
            item.title = @"";
            
        }else{
            NSLog(@"%@",self.finshWebUrl);
            UIBarButtonItem *item =self.webVC.navigationItem.leftBarButtonItems[1];
            item.title = @"关闭";
            
            
        }

        
        
    }

    
    
   
}

- (void)webViewDidStartLoad:(UIWebView *)webView
{
    NSLog(@"  start %@",webView.request.URL);
}



-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSLog(@"======%@",request.URL);
    if(self.finshWebUrl.length !=0){
  
        if ([request.URL isEqual:[NSURL URLWithString:self.finshWebUrl]]) {
            UIBarButtonItem *item =self.webVC.navigationItem.leftBarButtonItems[1];
            item.title = @"";
        }
        
    }
    
    
   

    return YES;
}




- (void)appGo:(CDVInvokedUrlCommand*)command{
    NSDictionary * argums = command.arguments[0];
    
    NSLog(@"====>>>>>>>>>>>>%@",argums);
    NSString *urlSchema = [argums objectForKey:@"urlSchema"];
    NSString *suffixText = [argums objectForKey:@"suffixText"];
//    NSString *appType = [argums objectForKey:@"appType"];//"appType": "appstore/inhouse/",
//    NSString *downUrl = [argums objectForKey:@"downloadUrl"];
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@://%@",urlSchema,suffixText]];


    
   
    if ( [[UIApplication sharedApplication] openURL:url]){
        
        NSLog(@"==appGo===");
        
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
        // NSString *appType = [_appInfo objectForKey:@"appType"];
        NSURL *downloadUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@",downloadUrlString]];
        //        应用未安装情况
        // if ([appType isEqualToString:@"inhouse"]) {
        //     //            通过跳转url下载app   downloadUrl
        //     [[UIApplication sharedApplication] openURL:downloadUrl];
            
        // }
        // if ([appType isEqualToString:@"appstore"]) {
            //                        通过跳转url下载app
            //            比如：http://itunes.apple.com/gb/app/yi-dong-cai-bian/id391945719?mt=8
            //            然后将 http:// 替换为 itms:// 或者 itms-apps://：
            
            [[UIApplication sharedApplication] openURL:downloadUrl];
        // }
        
    }
    if(buttonIndex == 0){
        NSLog(@"=msg:====cancel=====");
    }

    
}

-(void)appLiteGo:(CDVInvokedUrlCommand *)command
{

    NSString *url = command.arguments[0];
    NSLog(@"====>>>>>>>>>>>>%@",url);

    [self creactWebViewWithUrl:url];
}

@end
