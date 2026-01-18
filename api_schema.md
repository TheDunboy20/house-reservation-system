GET /houses/                                # Get all houses [done]
GET /houses/{id}                            # Get house by ID [done]
POST /houses                                # Create a new house [done]
PUT /houses/{id}                            # Update house by ID [done]
DELETE /houses/{id}                         # Delete house by ID [done]
GET /houses/{id}/house-days                 # Get house days by house ID [done]

GET     /reservations                        # List all (admin) or add filters [done]                                                
GET     /reservations/{id}                   # Get reservation details [done]                                                           
POST    /reservations                        # Create reservation [not needed]                                               
PUT     /reservations/{id}                   # Update reservation [done]                                                                   

GET     /users/{userId}/reservations         # Get user's reservations


{                                                                       
"name": "Beach House",                                                    
"description": "Beautiful 3-bedroom beach house with ocean views",        
"address": "123 Ocean Drive, Miami Beach, FL 33139",                      
"priceInCents": 25000,                                                    
"availableFrom": "2026-01-20",                                            
"availableTo": "2026-02-03"                                               
}