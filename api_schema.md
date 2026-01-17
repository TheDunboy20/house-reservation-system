GET /houses/                                # Get all houses
GET /houses/{id}                            # Get house by ID
POST /houses                                # Create a new house
PUT /houses/{id}                            # Update house by ID
DELETE /houses/{id}                         # Delete house by ID
GET /houses/{id}/house-days                 # Get house days by house ID

GET     /reservations                        # List all (admin) or add filters                                                      
GET     /reservations/{id}                   # Get reservation details                                                              
POST    /reservations                        # Create reservation                                                                   
PUT     /reservations/{id}                   # Update reservation                                                                   
DELETE  /reservations/{id}                   # Cancel reservation                                                                   
GET     /users/{userId}/reservations         # Get user's reservations