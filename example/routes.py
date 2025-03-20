from flask import request, jsonify
from db import db

# Route to get an item by ID
def get_item():
    item_id = request.args.get('id')
    item = db.get_item(item_id)
    if item:
        return jsonify({"id": item_id, "data": item}), 200
    return jsonify({"error": "Item not found"}), 404

# Route to add or update an item
def add_or_update_item():
    item_id = request.json.get('id')
    item_data = request.json.get('data')
    
    if db.update_item(item_id, item_data):
     
