$('.loginButton').click(function(){

 
  //stuff to happen after click goes here. E.g.
  // FACEBOOK LOGIN
   FB.login(function(response) {
   
      // stuff you want to happen after login goes here
	  
	   if (response.authResponse) {
         console.log('Welcome!  Fetching your information.... ');
         FB.api('/me', function(response) {
			  alert('Good to see you, ' + response.name + '.<br> Thank You! for connecting Wiki Lanka with your Facebook account' );
			  
      		window.location.replace("http://wikilanka.pixelzexplorer.org/welcome.php?" + window.location.search.replace( "?", "" ));
         
          });
        } else {
        console.log('User cancelled login or did not fully authorize.');
       }
	  
      

 
   },{scope: 'publish_actions'},{redirect_uri: 'http://www.pixelzexplorer.org'}); //FB.login
 
   // END - FACEBOOK LOGIN
 
}); //click



