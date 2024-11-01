<template>
    <div class="comment-panel-comment-list-comment-item">
        <h4>
            {{ comment.user.name }} disse:
        </h4>
        <span>
            {{ comment.text }}
        </span>
    </div>
</template>

<script>
export default {
    name: 'CommentPanelCommentListCommentItem',
    props: {
        comment: {
            type: Object,
            required: true
        }
    }
}
</script>

<style lang="scss" scoped>
.comment-panel-comment-list-comment-item {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

h4 {
    margin: 0;
}
</style>