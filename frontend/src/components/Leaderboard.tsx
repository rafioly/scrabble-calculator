import { Dialog, DialogTitle, DialogContent, List, ListItem, ListItemText, DialogActions, Button, Typography } from '@mui/material';

type LeaderboardProps = {
    isOpen: boolean;
    onClose: () => void;
    leaderboard: { word: string; score: number }[];
};

const Leaderboard = ({ isOpen, onClose, leaderboard }: LeaderboardProps) => {
    let dialogContent;

    if (leaderboard.length > 0) {
        dialogContent = (
            <List>
                {leaderboard.map((entry, index) => (
                    <ListItem key={index}>
                        <ListItemText primary={`${index + 1}. ${entry.word}`} secondary={`Score: ${entry.score}`} />
                    </ListItem>
                ))}
            </List>
        );
    } else {
        dialogContent = (
            <Typography variant="body1" align="center" sx={{ mt: 2 }}>
                No scores yet. Be the first to save a score!
            </Typography>
        );
    }

    return (
        <Dialog open={isOpen} onClose={onClose} fullWidth>
            <DialogTitle>Top 10 Scores</DialogTitle>
            <DialogContent sx={{ minHeight: 100 }}>
                {dialogContent}
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
};

export default Leaderboard;
