# db.py
class Database:
    def __init__(self):
        self.data = {}

    def get_item(self, item_id):
        return self.data.get(item_id)

    def add_item(self, item_id, item_data):
        self.data[item_id] = item_data

    def update_item(self, item_id, item_data):
        if item_id in self.data:
            self.data[item_id] = item_data
            return True
        return False

    def delete_item(self, item_id):
        if item_id in self.data:
            del self.data[item_id]
            return True
        return False

db = Database()
