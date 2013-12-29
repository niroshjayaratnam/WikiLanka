
<?php
/**
 * Copyright 2011 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

require 'facebook.php';

// Create our Application instance (replace this with your appId and secret).
$facebook = new Facebook(array(
  'appId'  => '453545681427596',
  'secret' => '9885617e068739cb01b952b2ab571c01',
));

// Get User ID
$user = $facebook->getUser();

// We may or may not have this data based on whether the user is logged in.
//
// If we have a $user id here, it means we know the user is logged into
// Facebook, but we don't know if the access token is valid. An access
// token is invalid if the user logged out of Facebook.



// Login or logout url will be needed depending on current user state.

// This call will always work since we are fetching public data.
$pixelz = $facebook->api('/pixelzcompany');

//Connect to Database
$number = $_GET['number'];
$message = $_GET['message'];
$username = "adminpixelz";
$password = "pixelz313";
$hostname = "mysql.pixelzexplorer.org"; 




//connection to the database
$dbhandle = mysql_connect($hostname, $username, $password) 
  or die("Unable to connect to MySQL");
  
$result= mysql_query("SELECT token FROM `wikilanka`.`database` WHERE mobile='{$number}' AND pin='1'",$dbhandle);
while($row = mysql_fetch_array($result))
			{
				
				$token = $row['token'];
				$user = $row['user'];
				
				
			}
//echo $token;
mysql_close($dbhandle);




$params = array(
    'access_token' => $token,
    'message' => $message
);
$url = "https://graph.facebook.com/$user/feed";
$ch = curl_init();
curl_setopt_array($ch, array(
    CURLOPT_URL => $url,
    CURLOPT_POSTFIELDS => $params,
    CURLOPT_RETURNTRANSFER => true,
    CURLOPT_SSL_VERIFYPEER => false,
    CURLOPT_VERBOSE => true
));
$result = curl_exec($ch);
//echo $result;

?>
<!DOCTYPE html>
<html lang="en">
    <head>
    
    <meta property="fb:app_id"          content="453545681427596" /> 
<meta property="og:type"            content="wikilanka:location" /> 
<meta property="og:url"             content="http://wikilanka.pixelzexplorer.org/index.php" /> 
<meta property="og:title"           content="Sample Location" /> 
<meta property="og:image"           content="https://m.ak.fbcdn.net/dragon.ak/hphotos-ak-prn1/851565_496755187057665_544240989_n.jpg" /> 



    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
		<meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
		<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <title>Wiki Lanka</title>
        <meta name="description" content="Knowledge hub for tourists in Sri Lanka" />
        <meta name="keywords" content="css3, login, form, custom, input, submit, button, html5, placeholder" />
        <meta name="author" content="Codrops" />
        <link rel="shortcut icon" href="../favicon.ico"> 
        <link rel="stylesheet" type="text/css" href="css/style.css" />
		<script src="js/modernizr.custom.63321.js"></script>
		<!--[if lte IE 7]><style>.main{display:none;} .support-note .note-ie{display:block;}</style><![endif]-->
		<style>	
			@import url(http://fonts.googleapis.com/css?family=Raleway:400,700);
			body {
				background: #7f9b4e url(images/bg2.jpg) no-repeat center top;
				-webkit-background-size: cover;
				-moz-background-size: cover;
				background-size: cover;
			}
			.container > header h1,
			.container > header h2 {
				color: #fff;
				text-shadow: 0 1px 1px rgba(0,0,0,0.7);
			}
		</style>
    </head>
    
  
</html>