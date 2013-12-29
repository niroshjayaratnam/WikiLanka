$('.loginButton').click(function(){

 
  //stuff to happen after click goes here. E.g.
  // FACEBOOK LOGIN
   FB.login(function(response) {
   
      // stuff you want to happen after login goes here
      FB.api('/me', function(response) {
        
        // stuff you want to happen after getting data goes here
      
        console.log(response);
 
      }); //FB.api

 
   },{scope: 'publish_actions'}); //FB.login
 
   // END - FACEBOOK LOGIN
 
}); //click
