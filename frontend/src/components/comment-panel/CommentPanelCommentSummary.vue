<template>
    <div class="comment-panel-comment-summary">
        <h2>
            Resumo dos comentários
        </h2>
        <span>
            {{ summary }}
        </span>
    </div>
</template>

<script>
export default {
    name: 'CommentPanelCommentSummary',
    props: {
        summary: {
            type: String,
            required: true
        }
    }
}
</script>

<style lang="scss" scoped>
.comment-panel-comment-summary {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 15px;
    border: 1px solid #FFD700;
    border-left: 5px solid #FFD700;
    background: linear-gradient(to bottom, #fff6c1, #ffee8d);
    border-radius: 5px;
    color: #000000;
}

h2 {
    margin: 0;
}
</style>