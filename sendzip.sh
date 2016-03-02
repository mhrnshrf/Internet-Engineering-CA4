if [ $? -eq 0 ]; then
    echo "Sent zip file."
    java -jar SendHTTPPOSTPacket-1.0.0-SNAPSHOT.jar -u http://localhost:9091/config/uploadzip -f type.zip
fi
