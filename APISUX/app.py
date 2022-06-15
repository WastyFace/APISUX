from flask import Flask, request
from flask_migrate import Migrate
from flask_restful import Api
from werkzeug.utils import secure_filename

from config import Config
from extensions import db, jwt


from resources.user import UserListResource, UserResource, MeResource
from resources.token import TokenResource, RefreshResource, RevokeResource, block_list
from resources.colonia import ColoniaListResource, ColoniaResource, ColoniaPublishResource
from resources.ruta import RutaListResource, RutaResource, RutaPublishResource


def create_app():
    app = Flask(__name__)
    app.config.from_object(Config)
    register_extensions(app)
    register_resources(app)

    return app

def register_extensions(app):
    db.init_app(app)
    migrate = Migrate(app, db)
    jwt.init_app(app)

    @jwt.token_in_blocklist_loader
    def check_if_token_in_blocklist(jwt_header, jwt_payload):
        jti = jwt_payload["jti"]
        return jti in block_list

def register_resources(app):
    api = Api(app)

    api.add_resource(UserListResource, '/users')
    api.add_resource(UserResource, '/users/<string:username>')

    api.add_resource(MeResource, '/me')

    api.add_resource(TokenResource, '/token')
    api.add_resource(RefreshResource, '/refresh')
    api.add_resource(RevokeResource, '/revoke')

    api.add_resource(ColoniaListResource, '/colonias')
    api.add_resource(ColoniaResource, '/colonias/<int:colonia_id>')
    api.add_resource(ColoniaPublishResource, '/colonias/<int:colonia_id>/publish')

    api.add_resource(RutaListResource, '/rutas')
    api.add_resource(RutaResource, '/rutas/<int:ruta_id>')
    api.add_resource(RutaPublishResource, '/rutas/<int:ruta_id>/publish')


if __name__ == '__main__':
    app = create_app()
    app.run(host='0.0.0.0', port=9090)
