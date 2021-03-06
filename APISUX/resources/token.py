from http import HTTPStatus
from flask import request
from flask_restful import Resource
from flask_jwt_extended import create_access_token
from flask_jwt_extended import create_refresh_token
from flask_jwt_extended import get_jwt
from flask_jwt_extended import get_jwt_identity
from flask_jwt_extended import jwt_required

from utils import check_password
from models.user import User

block_list = set()


class TokenResource(Resource):

    def post(self):

        json_data = request.get_json()

        username = json_data.get('username')
        password = json_data.get('password')

        user = User.get_by_username(username=username)

        if not user or not check_password(password, user.password):
            return {'message': 'username or password is incorrect'}, HTTPStatus.UNAUTHORIZED

        access_token = create_access_token(identity=user.id, fresh=True)
        refresh_token = create_refresh_token(identity=user.id)

        return {'access_token': access_token, 'refresh_token': refresh_token}, HTTPStatus.OK


class RefreshResource(Resource):

    @jwt_required(refresh=True)
    def post(self):
        current_user = get_jwt_identity()

        token = create_access_token(identity=current_user, fresh=False)

        return {'token': token}, HTTPStatus.OK


class RevokeResource(Resource):

    @jwt_required()
    def post(self):
        jti = get_jwt()['jti']

        block_list.add(jti)

        return {'message': 'Successfully logged out'}, HTTPStatus.OK
