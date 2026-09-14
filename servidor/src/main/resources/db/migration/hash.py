import bcrypt

password = input("Escribe la contraseña a codificar: ")

hashed = bcrypt.hashpw(password.encode(), bcrypt.gensalt(rounds=10))

print("Contraseña en texto plano:", password)
print("Contraseña codificada (BCrypt):", hashed.decode())