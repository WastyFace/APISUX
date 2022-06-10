from marshmallow import Schema, fields, post_dump, validate, validates, ValidationError

from schemas.user import UserSchema


class ColoniaSchema(Schema):
    class Meta:
        ordered = True

    id = fields.Integer(dump_only=True)
    nombre = fields.String(required=True, validate=[validate.Length(max=100)])
    codigoPostal = fields.Integer()

    created_at = fields.DateTime(dump_only=True)
    updated_at = fields.DateTime(dump_only=True)

    @post_dump(pass_many=True)
    def wrap(self, data, many, **kwargs):
        if many:
            return {'data': data}
        return data