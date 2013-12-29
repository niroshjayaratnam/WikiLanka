<?php
  // Remember to copy files from the SDK's src/ directory to a
  // directory in your application on the server, such as php-sdk/
   
  	$id = $_GET['id'];
	$number = $_GET['number'];
	$username = "adminpixelz";
	$password = "pixelz313";
	$hostname = "mysql.pixelzexplorer.org"; 
	
	
	// echo $number;

 $dbhandle = mysql_connect($hostname, $username, $password) 
  or die("Unable to connect to MySQL");
  $count = 0;
  $content;
  $token;
  $user; 
  $result = mysql_query("SELECT * FROM `wikilanka`.`places` WHERE `id`='{$id}'",$dbhandle);
  while ($row = mysql_fetch_array($result)){
	 // echo $row['mobile']. " " . $row['ID']. "<br>";
	  $count = $count + 1;
	  $content = $row;
  }
	// print_r($content);
	 $result= mysql_query("SELECT * FROM `wikilanka`.`database` WHERE mobile='{$number}' AND pin='1'",$dbhandle);
while($row = mysql_fetch_array($result))
			{
				
				$token = $row['token'];
				$user = $row['user'];
				
				
				
			}
echo $user;
   
  require 'facebook.php';

  $config = array(
    'appId' => '453545681427596',
    'secret' => '9885617e068739cb01b952b2ab571c01',
    'allowSignedRequest' => false // optional but should be set to false for non-canvas apps
  );

  $facebook = new Facebook($config);
  
  echo $content['thumbnail'];
 
 
?>
<html>
  <head></head>
  <body>

  <?php
    if(true) {
	 $attachment = array(
        'access_token'=>$token,
        'message'=>'I just visited this place in Sri Lanka',
		'name' => $content['name'],
		'caption' =>'Wiki Lanka',
		'description' =>$content['details'],
        'link' => 'http://www.wikilanka.pixelzexplorer.org/place.php?id='.$id,
		'picture' => $content['thumbnail']
    );
    $result = $facebook->api(
        $user."/feed",
		
        'post',
        $attachment
    );
	

     
       
    } else {

      // No user, so print a link for the user to login
      // To post to a user's wall, we need publish_stream permission
      // We'll use the current URL as the redirect_uri, so we don't
      // need to specify it here.
      $login_url = $facebook->getLoginUrl( array( 'scope' => 'publish_stream' ) );
	  echo 'Not Working';
      echo 'Please <a href="' . $login_url . '">login.</a>';

    } 

  ?>      

  </body> 
</html>  