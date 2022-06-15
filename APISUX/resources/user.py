from flask import request
from flask_restful import Resource
from flask_jwt_extended import create_access_token
from flask_jwt_extended import get_jwt_identity
from flask_jwt_extended import jwt_required
from flask_jwt_extended import JWTManager
from http import HTTPStatus
import json
from types import SimpleNamespace

from webargs import fields
from webargs.flaskparser import use_kwargs

from utils import hash_password

from models.user import User

from schemas.user import UserSchema

user_schema = UserSchema()
user_public_schema = UserSchema(exclude=('email', ))


class UserListResource(Resource):
    def post(self):

        json_data = request.get_json()

        try:
            data = user_schema.load(data=json_data)
        except ValidationError as exc:
            return {'message': "Validation errors", 'errors': exc.messages}, HTTPStatus.BAD_REQUEST

        user = User(**data)

        if User.get_by_username(user.username):
            return {'message': 'Username already used'}, HTTPStatus.BAD_REQUEST

        if User.get_by_email(user.email):
            return {'message': 'E-mail already used'}, HTTPStatus.BAD_REQUEST

        user.save()

        return user_schema.dump(user), HTTPStatus.CREATED


class UserResource(Resource):

    @jwt_required(optional=True)
    def get(self, username):

        user = User.get_by_username(username=username)

        if user is None:
            return {'message': 'user not found'}, HTTPStatus.NOT_FOUND

        current_user = get_jwt_identity()

        if current_user == user.id:
             data = user_schema.dump(user)

        else:
             data = user_public_schema.dump(user)

        return data, HTTPStatus.OK


class MeResource(Resource):

    @jwt_required()
    def get(self):
        user = User.get_by_id(id=get_jwt_identity())

        return user_schema.dump(user).data, HTTPStatus.OK
