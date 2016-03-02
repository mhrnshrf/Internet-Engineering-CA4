if [ $? -eq 0 ]; then
    echo "Starting Server"
    java -cp .:CA4.jar:coolserver.jar CA4
fi
