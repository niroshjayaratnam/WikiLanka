$('.loginButton').click(function(){

 
  //stuff to happen after click goes here. E.g.
  // FACEBOOK LOGIN
   FB.login(function(response) {
   
      // stuff you want to happen after login goes here
	  
	   if (response.authResponse) {
         console.log('Welcome!  Fetching your information.... ');
		 
         
        } else {
        console.log('User cancelled login or did not fully authorize.');
       }
	  
      

 
   }); //FB.login
 
   // END - FACEBOOK LOGIN
 
}); //click



