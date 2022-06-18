from extensions import db


class Colonia(db.Model):
    __tablename__ = 'colonia'

    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(100), nullable=False)
    codigoPostal = db.Column(db.Integer)
    created_at = db.Column(db.DateTime(), nullable=False, server_default=db.func.now())
    updated_at = db.Column(db.DateTime(), nullable=False, server_default=db.func.now(), onupdate=db.func.now())

    @classmethod
    def get_all(cls):
        return cls.query.all()

    @classmethod
    def get_by_id(cls, colonia_id):
        return cls.query.filter_by(id=colonia_id).first()

    @classmethod
    def get_by_nombre(cls, nombre):
        return cls.query.filter_by(nombre=nombre).first()

    def save(self):
        db.session.add(self)
        db.session.commit()

    def delete(self):
        db.session.delete(self)
        db.session.commit()
