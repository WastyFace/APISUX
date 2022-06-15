from extensions import db

class Ruta(db.Model):
    __tablename__ = 'ruta'

    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(100), nullable=False)
    recorrido = db.Column(db.String(600), nullable=False)
    colonias = db.Column(db.String(600), nullable=False)
    imgPath = db.Column(db.String(500), nullable=False)
    created_at = db.Column(db.DateTime(), nullable=False, server_default=db.func.now())
    updated_at = db.Column(db.DateTime(), nullable=False, server_default=db.func.now(), onupdate=db.func.now())

    @classmethod
    def get_all(cls):
        return cls.query.all()

    @classmethod
    def get_by_id(cls, ruta_id):
        return cls.query.filter_by(id=ruta_id).first()

    @classmethod
    def get_by_nombre(cls, nombre):
        return cls.query.filter_by(nombre=nombre).first()

    def save(self):
        db.session.add(self)
        db.session.commit()

    def delete(self):
        db.session.delete(self)
        db.session.commit()
