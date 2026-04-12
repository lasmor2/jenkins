def call(String image, String tag) {
    if (!image?.trim() || !tag?.trim()) {
        error "dockerInput: image and tag must not be empty"
    }
    if (image =~ /[;&|`\$<>\\\\]/ || tag =~ /[;&|`\$<>\\\\]/) {
        error "dockerInput: image or tag contains illegal shell characters"
    }
}
