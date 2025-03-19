from flask import Flask
from routes import get_item, add_or_update_item, delete_item

app = Flask(__name__)


@app.route("/item", methods=["GET"])
def item():
    return get_item()


@app.route("/item", methods=["PUT"])
def put_item():
    return add_or_update_item()


@app.route("/item", methods=["DELETE"])
def delete_item_route():
    return delete_item()


if __name__ == "__main__":
    app.run(debug=True)
