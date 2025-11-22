#!/bin/bash

INSTALL_DIR="$HOME/cae"
BIN_DIR="$INSTALL_DIR/bin"
JAR_SOURCE="./components/cae-cli.jar"
TEMPLATES_SOURCE="./components/file-templates"
COMPLETION_SOURCE="./components/cae_completion"
PROFILE_FILE="$HOME/.bashrc"

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

if [ -f "$COMPLETION_SOURCE" ]; then
  cp "$COMPLETION_SOURCE" "$INSTALL_DIR/cae_completion"
  source "$INSTALL_DIR/cae_completion"
  echo "🔧 Autocomplete script copied."
else
  echo "⚠️  Autocomplete script not found. Skipping."
fi

echo "🛠️  Creating CLI script at $BIN_DIR/cae..."
mkdir -p "$BIN_DIR"
cat <<EOF > "$BIN_DIR/cae"
#!/bin/bash
java -jar "\$CAE_CLI_HOME/cae-cli.jar" "\$@"
EOF

chmod +x "$BIN_DIR/cae"

sed -i '/CAE_CLI_HOME/d' "$PROFILE_FILE"
sed -i '/CAE_META_STRUCTURE_TEMPLATES_PATH/d' "$PROFILE_FILE"
sed -i '/PATH=.*CAE_CLI_HOME/d' "$PROFILE_FILE"
sed -i '/cae_completion/d' "$PROFILE_FILE"

echo "" >> "$PROFILE_FILE"
echo "# CAE CLI configuration" >> "$PROFILE_FILE"
echo "export CAE_CLI_HOME=\"$INSTALL_DIR\"" >> "$PROFILE_FILE"
echo "export CAE_META_STRUCTURE_TEMPLATES_PATH=\"$INSTALL_DIR/file-templates\"" >> "$PROFILE_FILE"
echo "export PATH=\"\$CAE_CLI_HOME/bin:\$PATH\"" >> "$PROFILE_FILE"
echo "source $INSTALL_DIR/cae_completion" >> "$PROFILE_FILE"

echo ""
echo "✅ Installation completed!"
echo "👉 Please run: source ~/.bashrc"
echo "👉 Then run: cae ls"
