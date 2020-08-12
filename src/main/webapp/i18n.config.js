module.exports = {
	languageConditionExpression(t) {
		return t.memberExpression(
			t.identifier('localStorage'),
			t.identifier('locale')
		);
	}
};
