package com.gate.tracker.ui.resources

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gate.tracker.data.local.entity.ResourceType

/**
 * Dialog for adding a new resource (PDF, Link, or Image)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddResourceDialog(
    selectedFileUri: String? = null,
    selectedFileName: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (title: String, uri: String, resourceType: ResourceType, thumbnailUrl: String?) -> Unit,
    onPickFile: (ResourceType) -> Unit,
    resolvedDriveMetadata: com.gate.tracker.data.drive.DriveManager.DriveFileMetadata? = null,
    onCheckUrlMetadata: (String) -> Unit
) {
    var title by remember(selectedFileName) { mutableStateOf(selectedFileName ?: "") }
    var uri by remember(selectedFileUri) { mutableStateOf(selectedFileUri ?: "") }
    var resourceType by remember { mutableStateOf(ResourceType.PDF) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var thumbnailUrl by remember { mutableStateOf<String?>(null) }
    
    // Auto-update title and type when resolved metadata changes
    LaunchedEffect(resolvedDriveMetadata) {
        if (resolvedDriveMetadata != null) {
            if (title.isBlank() || title == extractTitleFromUrl(uri)) {
                title = resolvedDriveMetadata.name
            }
            
            // Auto-detect type
            val mimeType = resolvedDriveMetadata.mimeType
            if (mimeType == "application/pdf") {
                resourceType = ResourceType.PDF
            } else if (mimeType.startsWith("image/")) {
                resourceType = ResourceType.IMAGE
            }
            // Keep as URL/Link if it's a folder or other type, but now we have the name!
            
            // Capture thumbnail
            thumbnailUrl = resolvedDriveMetadata.thumbnailLink
        }
    }
    
    // Update uri and title when file is selected
    LaunchedEffect(selectedFileUri, selectedFileName) {
        if (selectedFileUri != null) {
            uri = selectedFileUri
        }
        if (selectedFileName != null && title.isBlank()) {
            title = selectedFileName
        }
    }
    
    // Auto-fill title from URL when pasted
    LaunchedEffect(uri) {
        if (uri.isNotBlank() && title.isBlank() && resourceType == ResourceType.URL) {
            val isDriveUrl = uri.contains("drive.google.com") || uri.contains("docs.google.com")
            
            if (!isDriveUrl) {
                title = extractTitleFromUrl(uri)
            } else {
                onCheckUrlMetadata(uri)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Resource",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Resource Type Selection
                Text(
                    text = "Resource Type",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = resourceType == ResourceType.PDF,
                        onClick = { 
                            resourceType = ResourceType.PDF
                            errorMessage = null
                        },
                        label = { Text("PDF") },
                        leadingIcon = {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    )
                    
                    FilterChip(
                        selected = resourceType == ResourceType.IMAGE,
                        onClick = { 
                            resourceType = ResourceType.IMAGE
                            errorMessage = null
                        },
                        label = { Text("Image") },
                        leadingIcon = {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    )
                    
                    FilterChip(
                        selected = resourceType == ResourceType.URL,
                        onClick = { 
                            resourceType = ResourceType.URL
                            errorMessage = null
                        },
                        label = { Text("Link") },
                        leadingIcon = {
                            Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    )
                }

                Divider()

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        errorMessage = null
                    },
                    label = { Text("Title") },
                    placeholder = { Text("e.g., Important Formulas") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errorMessage?.contains("title", ignoreCase = true) == true
                )

                // URI/Link Input
                if (resourceType == ResourceType.URL) {
                    OutlinedTextField(
                        value = uri,
                        onValueChange = { newUri ->
                            uri = newUri
                            // Check for Drive metadata
                            val isDriveUrl = newUri.contains("drive.google.com") || newUri.contains("docs.google.com")
                            
                            // Auto-fill title from URL if title is empty (Skip for Drive URLs)
                            if (title.isBlank() && newUri.isNotBlank() && !isDriveUrl) {
                                title = extractTitleFromUrl(newUri)
                            }
                            
                            if (isDriveUrl) {
                                onCheckUrlMetadata(newUri)
                            }
                            
                            errorMessage = null
                        },
                        label = { Text("URL") },
                        placeholder = { Text("https://...") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        isError = errorMessage?.contains("URL", ignoreCase = true) == true
                    )
                } else {
                    // File picker button for PDF and Image
                    OutlinedButton(
                        onClick = { onPickFile(resourceType) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uri.isBlank()) 
                                "Pick ${if (resourceType == ResourceType.PDF) "PDF" else "Image"} File"
                            else
                                "File selected ✓"
                        )
                    }
                }

                // Error Message
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validation
                    when {
                        title.isBlank() -> {
                            errorMessage = "Title is required"
                        }
                        uri.isBlank() -> {
                            errorMessage = if (resourceType == ResourceType.URL)
                                "URL is required"
                            else
                                "Please select a file"
                        }
                        else -> {
                            onConfirm(title, uri, resourceType, thumbnailUrl)
                        }
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Extract a meaningful title from various URL types
 */
private fun extractTitleFromUrl(url: String): String {
    if (url.isBlank()) return ""
    
    return try {
        val uri = android.net.Uri.parse(url)
        val host = uri.host?.lowercase() ?: ""
        val path = uri.path ?: ""
        
        when {
            // Google Drive
            "drive.google.com" in host -> {
                // Extract from folder or file path
                val segments = path.split("/").filter { it.isNotBlank() }
                val name = segments.lastOrNull { it != "view" && it != "edit" && !it.startsWith("d") }
                    ?.replace("-", " ")
                    ?.replace("_", " ")
                    ?: "Drive Resource"
                name.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            }
            
            // YouTube
            "youtube.com" in host || "youtu.be" in host -> {
                uri.getQueryParameter("v")?.let { "YouTube: $it" } 
                    ?: path.substringAfterLast("/").takeIf { it.isNotBlank() }?.let { "YouTube: $it" }
                    ?: "YouTube Video"
            }
            
            // GitHub
            "github.com" in host -> {
                val parts = path.split("/").filter { it.isNotBlank() }
                if (parts.size >= 2) "${parts[0]}/${parts[1]}" else "GitHub Repository"
            }
            
            // GeeksforGeeks, other educational sites
            "geeksforgeeks.org" in host || "leetcode.com" in host || "stackoverflow.com" in host -> {
                path.split("/")
                    .lastOrNull { it.isNotBlank() }
                    ?.replace("-", " ")
                    ?.split(" ")
                    ?.take(5)
                    ?.joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                    ?: host.split(".").first().replaceFirstChar { it.uppercase() }
            }
            
            // Generic URL - use last path segment or domain
            else -> {
                path.split("/")
                    .lastOrNull { it.isNotBlank() && !it.contains("?") }
                    ?.replace("-", " ")
                    ?.replace("_", " ")
                    ?.split(" ")
                    ?.take(4)
                    ?.joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                    ?: host.removePrefix("www.").split(".").first().replaceFirstChar { it.uppercase() }
            }
        }
    } catch (e: Exception) {
        // Fallback to domain or empty
        url.removePrefix("https://").removePrefix("http://").split("/").first()
    }
}
