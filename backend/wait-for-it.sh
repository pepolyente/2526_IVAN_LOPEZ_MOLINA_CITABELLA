host="$1"
shift

until nc -z ${host%:*} ${host#*:}; do
  echo "⏳ Esperando a MySQL en $host..."
  sleep 2
done

echo "✅ MySQL disponible - arrancando backend"
exec "$@"