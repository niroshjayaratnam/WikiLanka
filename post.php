<? php
	$number = $_GET['number'];
	
	$username = "adminpixelz";
	$password = "pixelz313";
	$hostname = "mysql.pixelzexplorer.org"; 
	
	// echo $number;

 $dbhandle = mysql_connect($hostname, $username, $password) 
  or die("Unable to connect to MySQL");
	 
	 $result = mysql_query("SELECT * FROM `wikilanka`.`database` where `mobile`='$number'",$dbhandle);
	 
	 $row = mysql_fetch_array($result);
	 $token = $row['token'];
	 echo $token;
	 ?>
	 