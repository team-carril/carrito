## CARRITO

## Rutas

### /carts

#### GET
- Responses:
	- 200:
		- Description: 
			Returns list of Carts sorted by status
		- Content: 
			application/json
	
#### POST

- RequestBody:
	- Description: 
		User JSON
	- Requiered:
		TRUE
	- Content:
		application/json
		
- Responses:
	- 200:
		- Description: 
			New cart created
		- Content: 
			application/json
	- 400:
		- Description: 
			Wrong request body, must be User
		- Content:
			application/json
			
### /carts/{id}

#### PATCH

- RequestBody:
	- Description:
		ProductFromCatalog JSON
	- Requiered:
		TRUE
	- Content:
		application/json
		
- Responses:
	- 200:
		- Description:
			Cart Update
		- Content:
			application/json
	- 400:
		- Description:
			Wrong ProductFromCatalog JSON
		- Content:
			application/json
	- 404:
		- Description:
			Cart not found
		- Content: 
			application/json
	
#### DELETE

- Responses:
	- 200:
		- Description:
			Cart deleted
	- 400:
		- Description:
			Cart not found
		- Content:
			application/json
			
### /products/{id}

#### PATCH
- Description:
	Updates every product with matching id with the given requestBody
- RequestBody:
	- Description: ProductFromCatalog JSON
	- Requiered: TRUE
	- Content: application/json
- Responses:
	- 200:
		- Description:
			Products updated correctly
		- Content:
			application/json
	- 400:
		- Description:
			Wrong Product JSON
		- Content:
			application/json
	- 404:
		- Description:
			No product with the given id found
		- Content:
			application/json

#### DELETE

- Description:
	Deletes product with matching id with the given requestBody
- Responses:
	- 200:
		- Description:
			Products deleted correctly
		- Content:
			application/json
	- 404:
		- Description:
			No product with the given id found
		- Content:
			application/json
			
### carts/submit/{id}

#### PATCH

- Responses:
	- 200:
		- Description:
			Cart submited
	- 404:
		- Description:
			Cart not found
		- Content:
			application/json
