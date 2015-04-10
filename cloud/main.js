
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("deleteAnonUser", function(request, response) {

	Parse.Cloud.useMasterKey();

	var anonId = request.params.anonId;
	console.log("Anon User To Delete: " + anonId);

	var query = new Parse.Query(Parse.User);
	query.get(anonId, {
  		success: function(anonUser) {
    		anonUser.destroy({                                                                                                                                
        		success: function() {                                                                                                                       
          			response.success('User deleted');                                                                                                         
        		},                                                                                                                                          
        		error: function(error) {                                                                                                                    
          			response.error(error);                                                                                                                    
        		}                                                                                                                                           
      		});
  		},
  		error: function() {                                                                                                                        
      		response.error();                                                                                                                        
    	}
	});
});
