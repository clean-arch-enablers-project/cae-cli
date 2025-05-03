#!/bin/bash

INSTALL_DIR="$HOME/cae"
BIN_DIR="$INSTALL_DIR/bin"
JAR_SOURCE="./components/cae-cli.jar"
TEMPLATES_SOURCE="./components/file-templates"

echo "📦 Installing CAE CLI..."

if [ -d "$INSTALL_DIR" ]; then
  echo "✔ Directory $INSTALL_DIR already exists."
else
  echo "📁 Creating directory $INSTALL_DIR..."
  mkdir -p "$BIN_DIR"
  mkdir -p "$INSTALL_DIR/file-templates"
fi

cp "$JAR_SOURCE" "$INSTALL_DIR/cae-cli.jar"

cp -r "$TEMPLATES_SOURCE"/* "$INSTALL_DIR/file-templates"

echo "🛠️  Creating CLI script at $BIN_DIR/cae..."
mkdir -p "$BIN_DIR"
cat <<EOF > "$BIN_DIR/cae"
#!/bin/bash
java -jar "\$CAE_CLI_HOME/cae-cli.jar" "\$@"
EOF

chmod +x "$BIN_DIR/cae"

PROFILE_FILE="$HOME/.bashrc"
if ! grep -q "CAE_CLI_HOME" "$PROFILE_FILE"; then
  echo "" >> "$PROFILE_FILE"
  echo "# CAE CLI configuration" >> "$PROFILE_FILE"
  echo "export CAE_CLI_HOME=\"$INSTALL_DIR\"" >> "$PROFILE_FILE"
  echo "export CAE_META_STRUCTURE_TEMPLATES_PATH=\"$INSTALL_DIR/file-templates\"" >> "$PROFILE_FILE"
  echo "export PATH=\"\$CAE_CLI_HOME/bin:\$PATH\"" >> "$PROFILE_FILE"
  echo "✅ Environment variables added to $PROFILE_FILE"
else
  echo "🔁 Environment variables already configured in $PROFILE_FILE"
fi

echo ""
echo "✅ Installation completed!"
echo "👉 Please run: source ~/.bashrc"
echo "👉 Then run: cae ls"
