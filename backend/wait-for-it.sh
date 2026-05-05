#!/bin/sh
host="$1"
shift

if [ "$1" = "--" ]; then
  shift
fi

until nc -z ${host%:*} ${host#*:}; do
  echo "⏳ Esperando a MySQL en $host..."
  sleep 2
done

echo "✅ MySQL disponible - arrancando backend"
exec "$@"