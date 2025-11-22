#!/bin/bash

INSTALL_DIR="$HOME/cae"
JAR_SOURCE="./components/cae-cli.jar"
TEMPLATES_SOURCE="./components/file-templates"
COMPLETION_SOURCE="./components/cae_completion"
SHELL_PROFILE=""

if [ -d "$INSTALL_DIR" ]; then
  echo "✔ Found cae directory."
else
  echo "📁 Creating $INSTALL_DIR..."
  mkdir -p "$INSTALL_DIR/file-templates"
fi

echo "🔧 Creating cae.sh script..."
echo "#!/bin/bash" > "$INSTALL_DIR/cae.sh"
echo "java -jar \$CAE_CLI_HOME/cae-cli.jar \"\$@\"" >> "$INSTALL_DIR/cae.sh"
chmod +x "$INSTALL_DIR/cae.sh"

cp -f "$JAR_SOURCE" "$INSTALL_DIR"
cp -rf "$TEMPLATES_SOURCE"/* "$INSTALL_DIR/file-templates"

if [ -f "$COMPLETION_SOURCE" ]; then
  cp -f "$COMPLETION_SOURCE" "$INSTALL_DIR/cae_completion"
  source "$INSTALL_DIR/cae_completion"
  echo "✅ Autocomplete script installed."
else
  echo "⚠️ Autocomplete script not found. Skipping."
fi

if [ -f "$HOME/.zshrc" ]; then
  SHELL_PROFILE="$HOME/.zshrc"
elif [ -f "$HOME/.bash_profile" ]; then
  SHELL_PROFILE="$HOME/.bash_profile"
fi

if [ -n "$SHELL_PROFILE" ]; then
  sed -i '' '/CAE_CLI_HOME/d' "$SHELL_PROFILE"
  sed -i '' '/CAE_META_STRUCTURE_TEMPLATES_PATH/d' "$SHELL_PROFILE"
  sed -i '' '/PATH=.*CAE_CLI_HOME/d' "$SHELL_PROFILE"
  sed -i '' '/cae_completion/d' "$SHELL_PROFILE"
  sed -i '' '/alias cae=/d' "$SHELL_PROFILE"

  echo "" >> "$SHELL_PROFILE"
  echo "# CAE CLI configuration" >> "$SHELL_PROFILE"
  echo "export CAE_CLI_HOME=\"$INSTALL_DIR\"" >> "$SHELL_PROFILE"
  echo "export CAE_META_STRUCTURE_TEMPLATES_PATH=\"$INSTALL_DIR/file-templates\"" >> "$SHELL_PROFILE"
  echo "export PATH=\"\$CAE_CLI_HOME/bin:\$PATH\"" >> "$SHELL_PROFILE"
  echo "alias cae=\"$INSTALL_DIR/cae.sh\"" >> "$SHELL_PROFILE"
  echo "source $INSTALL_DIR/cae_completion" >> "$SHELL_PROFILE"

  echo "✅ Configurations updated in $SHELL_PROFILE"
else
  echo "⚠️ No shell profile found to update."
fi

echo ""
echo "✅ Installation completed!"
echo "👉 Please run: source $SHELL_PROFILE"
echo "👉 Then run: cae ls"
