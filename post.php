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
//echo $user;
   
  require 'facebook.php';

  $config = array(
    'appId' => '453545681427596',
    'secret' => '9885617e068739cb01b952b2ab571c01',
    'allowSignedRequest' => false // optional but should be set to false for non-canvas apps
  );

  $facebook = new Facebook($config);
  
 // echo $content['thumbnail'];
 
 
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

      $login_url = $facebook->getLoginUrl( array( 'scope' => 'publish_stream' ) );
	  //echo 'Not Working';
     // echo 'Please <a href="' . $login_url . '">login.</a>';

    } 

  ?>      
  
  <div class="container">
		
			<!-- Codrops top bar -->
            <div class="codrops-top"><p align="center">Product of Pixelz</div><!--/ Codrops top bar -->
			
			<header>
			
				<h1><strong>Wiki Lanka</strong></h1>
				<h2>Knowledge hub for tourists in Sri Lanka</h2>
				<div class="support-note">
					<span class="note-ie">Sorry, only modern browsers.</span>
				</div>
				
			</header>
			
			<section class="main">
			  <form class="form-4" action ="post.php">
		     			      <p align="center" >
			 <?php if ($user): ?>
   
      <br><p align="center"><img  src="https://graph.facebook.com/<?php echo $user; ?>/picture?width=9999&height=9999" width="200" height="200">
    <br>  <?php echo($user_profile[name]); ?>
</p><br><br>
      <p align="center"><strong><em>Your Place was Posted</em></strong><br><br></p>
	  <input type="text" class="input" name="id" hidden = "true" value = "1">
	  <input type="text" class="input" name="number" hidden = "true" value = "<?php echo $number ?>">
      <input class = "shareButton" type="submit" value="Share Your Place" >
    <?php else: ?>
    <?php endif ?>
			        
          
			      </p>       
				</form>				​
              <p align="center">
               <h2 align="center" > powered by <br>
              <a href="http://www.pixelzexplorer.org"><img src="images/Logo_Pixelz.png" width="124" height="50" alt="Pixelz" longdesc="http://www.pixelzexplorer.org"></a></h2></p>
			</section>
			
        </div>

  </body> 
</html>  