
<?php


require 'facebook.php';
$facebook = new Facebook(array(
  'appId'  => '453545681427596',
  'secret' => '9885617e068739cb01b952b2ab571c01',
));


$user = $facebook->getUser();

$pixelz = $facebook->api('/pixelzcompany');


//Connect to Database
$number = $_GET['number'];
$username = "adminpixelz";
$password = "pixelz313";
$hostname = "mysql.pixelzexplorer.org"; 

$token = $facebook->getAccessToken();
 //echo $number."<br>". $token;
//$profile = $facebook->api("/me",'GET');
//connection to the database
$dbhandle = mysql_connect($hostname, $username, $password) 
  or die("Unable to connect to MySQL");
  
mysql_query("UPDATE `wikilanka`.`database` SET `token`='{$token}' WHERE `mobile`='{$number}'",$dbhandle);
mysql_query("UPDATE `wikilanka`.`database` SET `user`='{$user}' WHERE `mobile`='{$number}'",$dbhandle);



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
    
    <body>
   
    <div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=453545681427596";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>
   <div id="fb-root"></div>
<script>
  window.fbAsyncInit = function() {
   
    FB.init({
      appId      : '453545681427596', 
      
      status     : true, // check the login status upon init?
      cookie     : true, // set sessions cookies to allow your server to access the session?
      xfbml      : true  // parse XFBML tags on this page?
    });

  
  };


  (function(d, debug){
     var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement('script'); js.id = id; js.async = true;
     js.src = "//connect.facebook.net/en_US/all" + (debug ? "/debug" : "") + ".js";
     ref.parentNode.insertBefore(js, ref);
   }(document, /*debug*/ false));
</script>
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
			<p align="center" >
			  <form class="form-4" action="status.php">
		     			      
			 <?php if ($user): ?>
			 
   
      <p align="center"><img  src="https://graph.facebook.com/<?php echo $user; ?>/picture?width=9999&height=9999" width="200" height="200">
    <br>  <?php //echo $profile['name']; ?>
</p>
	 <br>
      <h4 align="center">You are connected to Facebook</h4><br>
	  
	  
	  <p align="center">Please enter the following PIN Number to your Dialog USSD</p>
	  <h4 align="center">Your PIN Number:</h4>
		 <p align="center">
	  <?php 
			
			
			
			$query = mysql_query("SELECT PIN FROM `wikilanka`.`database`");
		
			$pin = 1;
			$array=array();
			$counter=1;
			while($row = mysql_fetch_array($query))
			{
				
				$array[$counter]=$row['PIN'];
				//echo $array[$counter];
				$counter++;
				
				
				
			}
				
				
				
			while(array_search($pin,$array)){
			
				$pin=rand(1000,9999);
			};
			
			  
		
			


			echo $pin;	  
	  
			mysql_query("UPDATE `wikilanka`.`database` SET `pin`='{$pin}' WHERE `mobile`='{$number}'",$dbhandle);
			mysql_close($dbhandle);
	  ?>
 <p align="center">	  
	  <br>
	  <input type="text" class="input" name="status" required="true">
	  <input type="text" class="input" name="number" hidden = "true" value = "<?php echo $number ?>">
      <input class = "shareButton" type="submit" value="Update Status" ><br>
	  </h3> 
	  
    <?php else: ?>
    <?php endif ?>
			        
          
			      </p>       
				</form>	
				
	
</p>			​
              <p align="center">
               <h2 align="center" > powered by <br>
              <a href="http://www.pixelzexplorer.org"><img src="images/Logo_Pixelz.png" width="124" height="50" alt="Pixelz" longdesc="http://www.pixelzexplorer.org"></a></h2></p>
			</section>
			
        </div>
      
<script type="text/javascript" src="script.js"></script>
    </body>
</html>