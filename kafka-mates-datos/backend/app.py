from flask import Flask
from flask_cors import CORS
from app.api_routes import bp as api_bp
from app.report_routes import report_bp

def create_app():
    app = Flask(__name__)
    CORS(app)
    app.register_blueprint(api_bp)
    app.register_blueprint(report_bp)  

    return app

if __name__ == '__main__':
    app = create_app()
    app.run(debug=True)
