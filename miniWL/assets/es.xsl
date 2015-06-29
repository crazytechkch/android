<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="no"/>
	<xsl:param name="date"/>
	<xsl:param name="style"/>
	<xsl:template match="/">
		<html>
			<head>
				<style>
					<xsl:value-of select="$style" disable-output-escaping="yes"/>
					div {width:100%; text-align:center}
				</style>
			</head>
			<body>
				<div id = "content">
					<div><xsl:copy-of select="//text[@date=$date]/date"/></div><br/>
					<div><xsl:value-of select="//text[@date=$date]/script" disable-output-escaping="yes"/></div><br/>
					<div style="text-align:left"><xsl:copy-of select="//text[@date=$date]/desc"/></div>
				</div>
				<script>
				</script>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>